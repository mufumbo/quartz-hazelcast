quartz-hazelcast
================

Simple solution to support quartz scheduling with multiple servers. This is just a lock in order to make only one server to work as scheduler. As soon as that server goes down another would get the lock. To distribute the jobs it's recommended for the scheduled tasks to trigger distributed tasks.