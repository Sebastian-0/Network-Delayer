# Network-Delayer
This program is a small utility intended to test the robustness of networking applications that use TCP/IP. It acts as a man-in-the-middle and allows you to specify a network delay in milliseconds, or it may block the connection completely. Note that the program only blocks packages that are sent on the application level, this means that the TCP implementation at the end points won't detect any timeouts or disconnects.

## Compiling
Just download the files and compile them, this program has no dependencies. Once it is compiled it can be run by launching the class Launcher (in the default-package). 

## License
This program is free to use as long as you comply to the GNU GPL v3 license (see LICENSE for details).