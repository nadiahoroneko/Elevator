package by.gstu.kharaneka.graph;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class TextAreaAppender extends AppenderSkeleton {
    private final Document document;
 
            public TextAreaAppender(JTextArea textArea) {
        document = textArea.getDocument();
    }
 
            @Override
    protected void append(final LoggingEvent event) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    document.insertString(document.getLength(), event.getRenderedMessage() + "\n", null);
                } catch (BadLocationException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
 
            @Override
    public void close() {
    }
 
            @Override
    public boolean requiresLayout() {
        return false;
    }
}
