package ss.fortberg.ssdp;

import ss.fortberg.util.Externalizator;
import ss.fortberg.util.FBLogger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class SsdpDiscovery implements FBLogger {

    private static final String SSDP_MULTICAST_ADDRESS = "239.255.255.250";
    private static final int SSDP_PORT = 1900;
    private static final String SSDP_SEARCH_MESSAGE =
        "M-SEARCH * HTTP/1.1\r\n" +
            "HOST: " + SSDP_MULTICAST_ADDRESS + ":" + SSDP_PORT + "\r\n" +
            "MAN: \"ssdp:discover\"\r\n" +
            "MX: 3\r\n" +
            "ST: ssdp:all\r\n" + // Search target: all devices
            "\r\n";

    public static String discoverDevices() throws IOException {
        InetAddress multicastAddress = InetAddress.getByName(SSDP_MULTICAST_ADDRESS);

        // Use a DatagramSocket for sending and receiving unicast responses
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setReuseAddress(true);
            socket.setSoTimeout(5000); // Set a timeout for receiving responses (5 seconds)

            DatagramPacket sendPacket = new DatagramPacket(
                SSDP_SEARCH_MESSAGE.getBytes(),
                SSDP_SEARCH_MESSAGE.length(),
                multicastAddress,
                SSDP_PORT
            );

            log.info("Sending SSDP M-SEARCH request...");
            socket.send(sendPacket);

            byte[] receiveBuffer = new byte[1024];
            while (true) {
                try {
                    DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                    socket.receive(receivePacket); // Blocks until a response is received or timeout occurs

                    String response = new String(receivePacket.getData(), 0, receivePacket.getLength());

                    if (response.contains("ikassa-smartx")) {
                        System.out.println("\n--- Device Found ---");
                        System.out.println("From: " + receivePacket.getAddress().getHostAddress());
                        System.out.println(response);
                        System.out.println("--------------------\n");
                        final var location = Arrays.stream(response.split("\n"))
                            .filter(line -> line.contains("LOCATION:")).findFirst()
                            .map(line -> line.replace("LOCATION:", "").trim())
                            .orElseGet(Externalizator::getTerminalIp);
                        return location;
                    }

                } catch (Exception e) {
                    System.out.println("SSDP discovery complete (timeout reached).");
                    break; // Exit loop when no more responses are received within the timeout
                }
            }
        }
        return null;
    }
}
