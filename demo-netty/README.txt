通道管道执行流

ChannelPipeline p = ...;
p.addLast("1", new InboundHandlerA());
p.addLast("2", new InboundHandlerB());
p.addLast("3", new OutboundHandlerA());
p.addLast("4", new OutboundHandlerB());
p.addLast("5", new InboundOutboundHandlerX());	//both of Inbound and Outbound

read inbound event
	执行顺序：1, 2, 3, 4, 5
	实际执行：1, 2, 5
write outbound event
	执行顺序：5, 4, 3, 2, 1
	实际执行：5, 4, 3

