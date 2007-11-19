/*
 * RefImage.java
 *
 * Created on November 18, 2007, 5:12 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mobilecurrencyreader;

/**
 *
 * @author tlrobinson
 */
    class RefImage {
        public String filename;
        public int value;
        public ByteBufferImage bbi;
        public double difference;
        public RefImage(String filename, int value) {
            this.filename = filename;
            this.value = value;
        }
    }