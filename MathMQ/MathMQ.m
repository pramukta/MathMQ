(* Mathematica Package *)

(* Created by the Wolfram Workbench Oct 5, 2012 *)

BeginPackage["MathMQ`", {"JLink`"}]
(* Exported symbols added here with SymbolName::usage *) 

RabbitConnect[exchange_String, queue_String, type_String, amqp_List] := 
	Module[{client},
		RabbitInit[];
		client = JavaNew["edu.georgetown.mathmq.RabbitMQClient", amqp[[1]], amqp[[2]], amqp[[3]], amqp[[4]], amqp[[5]]];
		client@connect[exchange, type, queue];
		Return[client];
	];

RabbitCollect[client_, t_, callback_Function] :=
	Module[{message, t0 = SessionTime[]},
		RabbitInit[];
		Return[
			Reap[
			While[SessionTime[] - t0 < t,
				message = client@receive[];	
				If[!SameQ[message, ""], Sow[{message, callback[message]}]];
			]]
		]
	];

RabbitClose[client_JavaObject] := client@close[];

Begin["`Private`"]
(* Implementation of the package *)

RabbitInit[] := Module[{packagePath, rabbitjar},
	InstallJava[];
	packagePath = ParentDirectory@DirectoryName[FindFile["MathMQ`"]];
	rabbitjar = FileNameJoin[{packagePath, "ext", "rabbitmq-client.jar"}];
	If[!MemberQ[JavaClassPath[], rabbitjar],
		AddToClassPath[rabbitjar]
	]
];

End[]

EndPackage[]

