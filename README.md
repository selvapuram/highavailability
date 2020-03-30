# highavailability
Optum Coding Challenge

# This project prequisite are :
1. JDK 1.8
2. Eclipse or STS(as an IDE)

# Build and Run Project
1. Import the project and run as like java project

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

# Explanation:

1. Assumed that input can contain 2 copies each.
2. The design has been implemented ConcurrentHashMap as the datastructure to store the cluster of nodes and files.
3. ConcurrentHashMap is chosen for parellel processing across nodes and then files are stored in HashSet to lookup in the time complexity of O(1) or O(constant time)
4. Nodes are parellel processed and files are stream processed from the down host, and then matched over the other hosts via concurrent lookup.
5. Intermediate operations are peeked and published in LinkedBlockingQueue.
6. Furthermore Queue is further polled synchronously, to allocate the random host apart from the source and down host, which again takes the time complexity O(no_of_files_in_down_host * no_of_hosts) time is optimized by concurrent processing.
7. Input nodes are restructed by map.remove on O(constant_time)
8. Regarding to the space complexity, The Actual input O(no_of_files_in_down_host * no_of_hosts) space, Blocking Queue takes the space complexity O(no_of_files)
9. Design Pattern Adapted are: Singleton(for Cluster Access), Strategy(partition algorithm)

# Improvements/ Self Analysis
1. Larger Data set (May be B-tree)
2. Two processing logics are required: 1. to find the sourcehost from fileset 2. to copy the file to random host
3. Can this be reduced producer - consumer pattern or event loop pattern ? when to aggregate and stop ?

# Feedback
Thanks for the question to research and learn more about concurrency, stream and big data sets.

