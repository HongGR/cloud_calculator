# cloud_calculator
If you type 10 + 10 in Client, the protocol changes to ADD 10 10 and sends it to server, and server takes ADD 10 10 as 10 + 10, calculates the arithmetic operation, gets a result of 20, and sends a protocol called ANSWER 20 to Client, and creates a cloud calculator that outputs result: 20.
The server must have multiple clients with simultaneous access (multithread)
An error message should be sent when the four-pronged operation is divided by zero and when there are no two numbers.
Client server information should be saved and utilized as a txt file.
