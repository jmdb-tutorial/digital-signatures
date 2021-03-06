package jmdbtutorial.crypto;

import org.junit.Test;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.Base64;

import static java.lang.String.format;
import static java.lang.System.out;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Test_CryptoHashing {

    @Test
    public void sha256_simple() throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update("foo".getBytes());
        String hex = DatatypeConverter.printHexBinary(md.digest());
        System.out.println(hex.toLowerCase());
    }

    @Test
    public void sha256_hash() throws Exception {
        String input = "This is a string that we want to hash";


        ByteArrayInputStream dataInputStream = new ByteArrayInputStream(input.getBytes("UTF-8"));

        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");


        DigestInputStream in = new DigestInputStream(dataInputStream, messageDigest);

        byte[] buffer = new byte[8192];
        while (in.read(buffer) != -1) ;

        byte[] raw = messageDigest.digest();


        out.println("Input            : " + input);
        out.println("Digest Length    : " + messageDigest.getDigestLength());
        out.println("Algorithm        : " + messageDigest.getAlgorithm());


        String base64Hash = new String(Base64.getEncoder().encodeToString(raw));

        out.println("\nbase64           : " + base64Hash);
        out.println("hex              : " + printHexBytes(raw, 0).trim() + "\n");
        out.println("hex              : " + printHexBytes(raw, 1).trim() + "\n");

        printByteArray(raw);

    }

    /**
     * https://www.cs.cornell.edu/~tomf/notes/cps104/twoscomp.html
     * https://stackoverflow.com/questions/13109802/why-does-anding-a-number-convert-between-signed-and-unsigned-presentation
     * http://www.cs.uwm.edu/~cs151/Bacon/Lecture/HTML/ch03s09.html
     * https://stackoverflow.com/questions/6393873/how-to-get-the-binary-values-of-the-bytes-stored-in-byte-array
     * https://stackoverflow.com/questions/12310017/how-to-convert-a-byte-to-its-binary-string-representation/12310078#12310078
     */
    @Test
    public void convert_unsigned_to_signed() {
        byte b = -128;
        int unsignedInt = Byte.toUnsignedInt(b);

        int mask = 0xff;

        out.println(format("signed byte     : %4d", b));
        out.println(format("unsigned int    : %4d", unsignedInt));
        out.println(format("mask            : %4d", mask));
        out.println(format("mask (binary)   : %s", formatBinaryString(Integer.toBinaryString(mask), 4, 4)));
        out.println(format("signed (binary) : %s", formatBinaryString(Integer.toBinaryString(b), 4, 4)));
        out.println(format("b & 0xff        : %s", formatBinaryString(Integer.toBinaryString(b & mask), 4, 4)));

        //int i = 0b11111111111111111111111110000000;
        int i = 0b10000000000000000000000000000000;

        out.println("i   = " + i);
        out.println("min = " + Integer.MIN_VALUE);
    }

    /**
     * http://stackoverflow.com/a/6393904
     */
    public static String toBinaryString(byte b) {
        return Integer.toBinaryString(b & 0xff | 0x100).substring(1);
    }

    /**
     * https://stackoverflow.com/questions/141525/what-are-bitwise-shift-bit-shift-operators-and-how-do-they-work
     */
    @Test
    public void bitshifting_bytes() {
        byte b = (byte) 0b10000000;


        out.println("b       : " + padString("" + b, ' ', 8));
        out.println("b       : " + toBinaryString(b));
        out.println("b << 1  : " + toBinaryString((byte) (b << 1)));
        out.println("b >> 1  : " + toBinaryString((byte) ((b & 0xff) >> 1)));
        out.println("b >>> 1 : " + toBinaryString((byte) (b >>> 1)));

    }

    @Test
    public void bitmasking() {
        final byte FLAG_A = 0b00000001; // 1
        final byte FLAG_B = 0b00000010; // 2
        final byte FLAG_C = 0b00000100; // 4
        final byte FLAG_D = 0b00001000; // 8
        final byte FLAG_E = 0b00010000; // 16
        final byte FLAG_F = 0b00100000; // 32
        final byte FLAG_G = 0b01000000; // 64
        final byte FLAG_H = (byte) 0b10000000; //128

        byte flags = FLAG_A | FLAG_G | FLAG_E;

        out.println("flags: " + toBinaryString(flags));

        assertThat((flags & FLAG_A) == FLAG_A, is(true));
        assertThat((flags & FLAG_B) == FLAG_B, is(false));
        assertThat((flags & FLAG_C) == FLAG_C, is(false));
        assertThat((flags & FLAG_D) == FLAG_D, is(false));
        assertThat((flags & FLAG_E) == FLAG_E, is(true));
        assertThat((flags & FLAG_F) == FLAG_F, is(false));
        assertThat((flags & FLAG_G) == FLAG_G, is(true));
        assertThat((flags & FLAG_H) == FLAG_H, is(false));

    }


    @Test
    public void print_signed_bytes_as_binary() {
        for (int b = -128; b < 128; ++b) {
            out.println(format("%5d %s", (byte) b, toBinaryString((byte) b)));
        }
    }

    @Test
    public void print_signed_integers_as_binary() {
        int a = Integer.MIN_VALUE;
        int b = Integer.MIN_VALUE + 1;
        int c = (int) -Math.pow(2, 16) + 1;
        int d = -1;
        int e = 0;
        int f = 1;
        int g = Integer.MAX_VALUE;


        out.println(format("%11d", a) + " " + formatBinaryString(Integer.toBinaryString(a), 4, 4));
        out.println(format("%11d", b) + " " + formatBinaryString(Integer.toBinaryString(b), 4, 4));
        out.println(format("%11d", c) + " " + formatBinaryString(Integer.toBinaryString(c), 4, 4));
        out.println(format("%11d", d) + " " + formatBinaryString(Integer.toBinaryString(d), 4, 4));
        out.println(format("%11d", e) + " " + formatBinaryString(Integer.toBinaryString(e), 4, 4));
        out.println(format("%11d", f) + " " + formatBinaryString(Integer.toBinaryString(f), 4, 4));
        out.println(format("%11d", g) + " " + formatBinaryString(Integer.toBinaryString(g), 4, 4));
    }


    @Test
    public void byte_buffers() throws Exception {
        byte[] arr = {0x00, 0xF};
        ByteBuffer wrapped = ByteBuffer.wrap(arr).order(ByteOrder.BIG_ENDIAN);

        short num = wrapped.getShort(); // 1
        out.println("num= " + num);


        ByteBuffer dbuf = ByteBuffer.allocate(2);
        dbuf.putShort(num);
        byte[] bytes = dbuf.array(); // { 0, 1 }
        out.println(printRawBytes(bytes));
    }

    @Test
    public void bitmask_nibbles() {
        final byte BYTE = 0b01111111;
        final byte SHIFTED = BYTE >> 4;

        byte[] bytes = {BYTE}; // because a byte is a signed integer in java, cant set it to 0xff

        out.println(printRawBytes(bytes));
        out.println(printHexBytes(bytes));
        out.println(printBytesBinary(bytes));

        out.println(printBytesBinary(new byte[] {(BYTE & 0xF)}));

        out.println(printBytesBinary(new byte[] {(SHIFTED & 0xF)}));
    }

    private static final char[] hexCode = "0123456789abcdef".toCharArray();


    public static String printHexBytes(byte[] data) {
        return printHexBytes(data, 7);
    }

    /**
     * The bitshift operators return ints, not more bytes!
     *
     * So the problem is that when you bitshift right, your actually bitshifting a lot more bytes
     * than you bargained for because it returns an integer because an integer has 4 bytes
     * And when you cast it up from a byte it pads the left with 1111111's
     *
     *  So when you bitshift >> 4 you actually end up with
     *  From
     *  1111 0000
     *  to
     *  1111 1111
     *  When you WANTED
     *  0000 1111
     *
     *  So you then mask off the left hand 4 bits by ANDing with the mask:
     *  1111 0000
     *  Which is (0xf)
     */
    @Test
    public void shift_without_mask() {
        byte a = -16;
        out.println("Mask        : " + printBytesBinary(new byte[]{0xf}));
        out.println("Input       : " + printBytesBinary(new byte[]{a}));
        out.println(">> 4        : " + printBytesBinary(new byte[] {(byte)(a >> 4)}));
        out.println(">> 4 & 0xf  : " + printBytesBinary(new byte[] {(byte)((a >> 4) & 0xf)}));

        out.println("\nas Int            :  " + Integer.toBinaryString((int) a));
        out.println(">> 4 as Int       :  " + Integer.toBinaryString((a >> 4)));
        out.println(">> 4 & 0xf as Int :  " + Integer.toBinaryString((a >> 4) & 0xf));
        out.println("As int >> 4       :  " + (a >> 4));
        out.println("As int >> 4 & 0xf :  " + ((a >> 4) & 0xf));

    }

    /**
     * http://www.javamex.com/tutorials/conversion/decimal_hexadecimal.shtml
     * Also by looking in DataTypeconverter
     * https://en.wikipedia.org/wiki/Hexadecimal
     *
     * A byte (octet) contains 8 bits.
     * e.g. 10001111
     *
     * This would be represented in hex as:
     * 0x8f
     *
     * f    f
     * 1111 1111
     *
     * Each hex number represents a "nibble" (semi-octet) or 4 bits
     *
     * If you want to extract the nibbles, you first go for the one on the left (most significant)
     * And SHIFT 4 to the right to give:
     * 0    f
     * 0000 1111
     *
     * To get the one on the right (least significant), simply mask off the left hand bits
     */
    public static String printHexBytes(byte[] data, int padding) {

        StringBuilder r = new StringBuilder(data.length * 2);
        for (byte b : data) {
            r.append(padString("", ' ', padding));
            r.append(hexCode[(b >> 4) & 0xf]);
            r.append(hexCode[(b & 0xf)]);
        }
        return r.toString();

    }


    @Test
    public void bytes_and_bits() {
        byte[] bytes = new byte[]{0, 1, -1};
        printByteArray(bytes);


    }

    @Test
    public void printOutAString() throws UnsupportedEncodingException {
        String input = "ABC€$&\u00E8";

        out.println("String   : " + input);
        out.println("\nUTF-8");
        out.println("-----");

        StringBuilder sb = new StringBuilder();
        for (Character c : input.toCharArray()) {
            String s = c.toString();
            sb.append(padString(c.toString(), ' ', 9 * s.getBytes("UTF-8").length));

        }
        out.println("Input            : " + sb.toString());
        printByteArray(input.getBytes("UTF-8"));
    }

    public static void printByteArray(byte[] bytes) {
        int[] unsignedBytes = unsignedBytes(bytes);

        out.println(format("bytes.length     : %9d", bytes.length));
        out.println("bytes (signed)   : " + printRawBytes(bytes));
        out.println("bytes (unsigned) : " + printIntBytes(unsignedBytes));
        out.println("bytes (hex)      : " + printHexBytes(bytes));
        out.println("bytes (binary)   : " + printIntBytesBinary(unsignedBytes));
    }

    public static String printBytesBinary(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(" ").append(padString(formatBinaryString(Integer.toBinaryString(Byte.toUnsignedInt(b))), '0', 8));
        }
        return sb.toString();
    }

    public static String printIntBytesBinary(int[] unsignedBytes) {
        StringBuilder sb = new StringBuilder();
        for (int b : unsignedBytes) {
            sb.append(" ").append(padString(formatBinaryString(Integer.toBinaryString(b)), '0', 8));
        }
        return sb.toString();
    }

    private static String padString(String input, char padding, int width) {
        if (input.length() >= width) {
            return input;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < (width - input.length()); ++i) {
            sb.append(padding);
        }
        sb.append(input);
        return sb.toString();
    }

    private static int[] unsignedBytes(byte[] bytes) {
        int[] result = new int[bytes.length];

        for (int i = 0; i < bytes.length; ++i) {
            result[i] = bytes[i] & 0xFF;
        }
        return result;
    }

    public static String formatBinaryString(String binaryString) {
        return formatBinaryString(binaryString, 8, 0);
    }

    public static String formatBinaryString(String binaryString, int blockSize, int paddingBytes) {
        String paddedBinaryString = padBinaryStringWithZeros(binaryString, paddingBytes);

        StringBuilder sb = new StringBuilder();


        for (int i = 0; i < paddedBinaryString.length(); ++i) {
            sb.append(paddedBinaryString.charAt(i));
            if ((i + 1) % blockSize == 0) {
                sb.append(" ");
            }
        }
        return sb.toString().trim();
    }

    private static String padBinaryStringWithZeros(String binaryString, int paddingBytes) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < (paddingBytes * 8) - binaryString.length(); ++i) {
            sb.append("0");
        }
        sb.append(binaryString);
        return sb.toString();
    }

    @Test
    public void bigend_littlend() throws Exception {
        String bigendian = "00000000000000001e8d6829a8a21adc5d38d0a473b144b6765798e61f98bd1d";
        String littlendian = reverseHex(bigendian);

        out.println("Big Endian    : " + bigendian);
        out.println("Little Endian : " + littlendian);
    }

    public static String printRawBytes(byte[] bytes) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < bytes.length; ++i) {
            Byte B = bytes[i];
            sb.append(format("%9d", B.intValue()));
        }
        return sb.toString();
    }

    public static String printIntBytes(int[] bytes) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < bytes.length; ++i) {
            int l = bytes[i];
            sb.append(format("%9d", l));
        }
        return sb.toString();
    }

    public static String printUnsignedBytes(byte[] bytes) {
        return printUnsignedBytes(bytes, 9);
    }

    public static String printUnsignedBytes(byte[] bytes, int padding) {
        final String formatString = format("%%%dd", padding);

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < bytes.length; ++i) {
            Byte B = bytes[i];
            sb.append(format(formatString, B.intValue() & 0xFF));
        }
        return sb.toString();
    }

    public static String reverseHex(String originalHex) {
        int lengthInBytes = originalHex.length() / 2;
        char[] chars = new char[lengthInBytes * 2];
        for (int index = 0; index < lengthInBytes; index++) {
            int reversedIndex = lengthInBytes - 1 - index;
            chars[reversedIndex * 2] = originalHex.charAt(index * 2);
            chars[reversedIndex * 2 + 1] = originalHex.charAt(index * 2 + 1);
        }
        return new String(chars);
    }
}
