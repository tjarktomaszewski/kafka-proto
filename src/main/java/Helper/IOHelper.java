package Helper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class IOHelper {
    public static int readVarInt(DataInputStream in) throws IOException {
        int value = 0;
        int i = 0;
        int b;
        while (((b = in.readByte()) & 0x80) != 0) {
            value |= (b & 0x7F) << i;
            i += 7;
            if (i > 28) throw new IOException("VarInt too long");
        }
        value |= b << i;
        return value;
    }

    public static String readCompactString(DataInputStream in) throws IOException {
        int length = readVarInt(in) - 1; // decode length
        if (length < 0) return null;
        byte[] bytes = in.readNBytes(length);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     * Schreibt einen UnsignedVarInt (Kafka VarInt-Encoding).
     *
     * - Zahlen werden nicht immer auf 4 Bytes geschrieben (wie bei writeInt()),
     *   sondern in einer variablen Länge.
     * - Vorteil: kleine Zahlen (z. B. 0..127) brauchen nur 1 Byte.
     * - Prinzip: jeweils 7 Bits Nutzdaten pro Byte, höchstes Bit = "Fortsetzungs-Flag".
     *
     * Beispiel:
     *  - 5  (binär 00000101)  -> 05
     *  - 300(binär 100101100) -> AC 02
     */
    public static void writeUnsignedVarInt(DataOutputStream out, int value) throws IOException {
        // Solange noch mehr als 7 Bits übrig sind
        while ((value & 0xFFFFFF80) != 0L) {
            // Nimm die unteren 7 Bits und setze das MSB (0x80),
            // um zu signalisieren: "es folgen noch weitere Bytes"
            out.writeByte((value & 0x7F) | 0x80);

            // Schiebe die Zahl um 7 Bits nach rechts,
            // damit im nächsten Durchlauf die nächsten 7 Bits geschrieben werden.
            value >>>= 7;
        }

        // Letzte 7 Bits (ohne MSB = 0, weil nichts mehr folgt)
        out.writeByte(value & 0x7F);
    }
}
