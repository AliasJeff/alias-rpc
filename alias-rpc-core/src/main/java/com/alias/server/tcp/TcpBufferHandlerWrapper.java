package com.alias.server.tcp;

import com.alias.constant.ProtocolConstant;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.parsetools.RecordParser;

public class TcpBufferHandlerWrapper implements Handler<Buffer> {

    private final RecordParser recordParser;

    public TcpBufferHandlerWrapper(Handler<Buffer> bufferHandler) {
        recordParser = initRecordParser(bufferHandler);
    }

    @Override
    public void handle(Buffer buffer) {
        recordParser.handle(buffer);
    }

    private RecordParser initRecordParser(Handler<Buffer> bufferHandler) {
        RecordParser parser = RecordParser.newFixed(ProtocolConstant.MESSAGE_HEADER_LENGTH);
        parser.setOutput(new Handler<Buffer>() {
            int size = -1;
            // Full message buffer (header + body)
            Buffer resultBuffer = Buffer.buffer();
            @Override
            public void handle(Buffer buffer) {
                if (-1 == size) {
                    size = buffer.getInt(ProtocolConstant.MESSAGE_HEADER_LENGTH - 4);
                    parser.fixedSizeMode(size);
                    // Write the header to the result buffer
                    resultBuffer.appendBuffer(buffer);
                } else {
                    // Write the body to the result buffer
                    resultBuffer.appendBuffer(buffer);
                    // Handle the full message
                    bufferHandler.handle(resultBuffer);
                    // Reset the parser
                    parser.fixedSizeMode(ProtocolConstant.MESSAGE_HEADER_LENGTH);
                    size = -1;
                    resultBuffer = Buffer.buffer();
                }
            }
        });

        return parser;
    }
}
