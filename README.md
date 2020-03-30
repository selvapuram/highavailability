# highavailability
Optum Coding Challenge

This project prequisite are :
1. JDK 1.8
2. Eclipse or STS(as an IDE)

# Problem Statement

A cluster has multiple hosts and each host has multiple files stored in it.

Following is an example of 3 node cluster with multiple files in each of the nodes.

Input
##########
host1: file1, file2, file4, file5

host2: file2, file3

host3: file1, file3, file4

host4: file5
##########

Details:
1. There would be 2 copies of a single file across different nodes. Ex: file1 is available in both host1 and host3.
2. This is to ensure high availability of data, even if a host goes down, the file can be recovered from a different host.
3. Redundant copy of the file will not be stored in the same node.
4. For ex: 2 copies of file1 will not be stored in host1 since it doesn't make sense to store all the copies of file in the same host.

Given a host or list of hosts that went down, following needs to be identified.
1. File that should be copied from the host that went down.
2. What is the source host from which the file can be copied?
3. Any random destination host (that is neither source host nor the host that went down)

For ex: say if host2 is down

Code should return the following
<file to be copied>, <from which other source host file can be copied>, <random destination host which is not source host or the host that went down>
file2, host1, host3
file3, host3, host4 

In addition to the above, input structure should be modified as below so that it can be used as input if some other host goes down the next time.
######################
host1: file1, file2, file4, file5

host3: file1, file3, file4, file2

host4: file5, file3
######################

1. Assume that a max of 2 hosts can go down at a time.
2. Add sufficient unit test cases
3. If there are any assumptions please specify as part of code documentation
4. Please specify the complexity of the algorithm both space and runtime
5. Assume that cluster contains 1000s of hosts and each hosts contains 100K files
6. Assume an appropriate data structure for input, no need to parse the input from a file.
7. Identify appropriate data structure for representing output of the code.
8. Preferably code should be written in object oriented language
9. If design patterns are used, please specify as part of code documentation.
10. Assume that there is sufficient disk space in each of the host, so that there is no need to check if there is sufficient space in destination host before selecting it.
