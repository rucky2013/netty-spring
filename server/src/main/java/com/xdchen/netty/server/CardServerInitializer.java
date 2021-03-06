package com.xdchen.netty.server;

import com.xdchen.netty.handler.AddUserHandler;
import com.xdchen.netty.handler.dispatch.HandlerDispatcher;
import com.xdchen.netty.model.Room;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class CardServerInitializer extends ChannelInitializer<SocketChannel> {
    private int timeout = 3600;
    private HandlerDispatcher handlerDispatcher;
    private Room room = new Room();

    public void init() {
        new Thread(this.handlerDispatcher).start();
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(64*1024));
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(new AddUserHandler(room));
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        pipeline.addLast(new ReadTimeoutHandler(this.timeout));
        pipeline.addLast(new ServerAdapter(this.handlerDispatcher, room));
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setHandlerDispatcher(HandlerDispatcher handlerDispatcher) {
        this.handlerDispatcher = handlerDispatcher;
    }

    public Room getRoom() {
        return room;
    }
}
