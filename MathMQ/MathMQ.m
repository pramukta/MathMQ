(* Mathematica Package *)

(* Created by the Wolfram Workbench Oct 5, 2012 *)

BeginPackage["MathMQ`", {"JLink`"}]
(* Exported symbols added here with SymbolName::usage *) 

RabbitConnect[exchange_String, queue_String, type_String, amqp_List] := 
	Module[{client = JavaNew["edu.georgetown.mathmq.RabbitMQClient", amqp[[1]], amqp[[2]], amqp[[3]], amqp[[4]], amqp[[5]]]},
		Print[client@connect[exchange, type, queue]];
		Return[client];
	];

Begin["`Private`"]
(* Implementation of the package *)

$RABBITLIB = "E:\\rabbitmq-java-client";

InstallJava[];
AddToClassPath[FileNameJoin[{$RABBITLIB, "rabbitmq-client.jar"}]];
LoadJavaClass["edu.georgetown.mathmq.RabbitMQClient"];

End[]

EndPackage[]

