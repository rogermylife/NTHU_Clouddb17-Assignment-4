# Assignment 4
In this assignment, you are asked to implement the conservative concurrency manager.

## Steps
To complete this assignment, you need to

1. Fork the Assignment 4 project
2. Trace the code in the `as4-vanillacore` project's `org.vanilladb.core.storage.tx` and `org.vanilladb.core.storage.tx.concurrency` package yourself
3. Create your own `ConservativeConcurrencyMgr`
4. Modify the stored procedure API and stored procedures to accommodate read-/write-sets and use your concurrency manager
5. Remember to modify the property file (vanilladb.properties) and set the concurrency managers to your new one
6. Write a report

## Conservative Locking

In the conservative locking approach, we lock the read/write sets of a given transaction at once (atomically) **before execution**. Since the locks are retained in the beginning, there are no interleaving for conflicting txs and can thus prevent deadlocks. It performs well only if there are no/very few long txs. To know which objects to lock before execution, we have to collect the read/write sets in the stored procedures.

In this assignment, you are asked to implement the conservative concurrency manager that extends `ConcurrencyMgr`. You can trace the other concurrency managers like `SerializableConcurrencyMgr` as a reference to implement your own. Note that, you should not modify the `ConcurrencyMgr` interface. You can simply override and leave some methods blank if you think they are no longer necessary in the conservative implementation.

The workload we provide only access the data with their primary keys instead of predicates(e.g. you don't have to take care of the query such as "find all users with balance > 100.0$"). But you should at least implement MGL to **precent phantom due to inserts**.

After creating your own ConcurrencyMgr, you may have to modify the `StoredProcedure` API and the `MicroBenchmarkProc` so that you can collect the read/write objects before executing a transaction.

## Hint

- To aquire the read/write set atomically, you may want to let this process executed serializely (e.g. in a critical section)
- You will need a speicial object to represent the **Key** of the record, which can uniquely identify a record, for locking. You can create one by yourselves or use our RecordKey. Following is the template class of a RecordKey :

```Java
public class RecordKey  {

	private String tableName;
	private Map<String, Constant> keyEntryMap;

	public RecordKey(String tableName, Map<String, Constant> keyEntryMap) {
    // TODO: Figure it out yourself
	}

	public String getTableName() {
    // TODO: Figure it out yourself
	}

	public Constant getKeyVal(String fld) {
    // TODO: Figure it out yourself
	}

    @Override
    public boolean equals(Object obj) {
      // TODO: Figure it out yourself
    }

    @Override
    public int hashCode() {
      // TODO: Figure it out yourself
    }

}

```

## The Report

- How you implement
  - API changes and/or new classes
- Compare the throughputs before and after your modification using the given benchmark & loader (You should use more RTE to demonstrate the concurrent execution behaviour)
- Observe and discuss the impact of buffer pool size to your new system

	Note: There is no strict limitation to the length of your report. Generally, a 2~3 pages report with some figures and tables is fine. **Remember to include all the group members' student IDs in your report.**

## Submission

The procedure of submission is as following:

1. Fork our Assignment 4 on GitLab
2. Clone the repository you forked
3. Finish your work and write the report
4. Commit your work, push your work to GitLab.
  - Name your report as `[Team Member 1 ID]_[Team Member 2 ID]_assignment2_report`
    - E.g. `102062563_103062528_assignment2_reprot.pdf`
5. Open a merge request to the original repository.
  - Source branch: Your working branch.
  - Target branch: The branch with your team number. (e.g. `team-1`)
  - Title: `Team-X Submission` (e.g. `Team-1 Submission`).

**Important: We do not accept late submission.**

## No Plagiarism Will Be Tolerated

If we find you copy someone’s code, you will get 0 point for this assignment.

## Demo

Due to the complexity of this assignment, we hope you can come to explain your work face to face. We will announce a demo table after submission. Don't forget to choose the demo time for your team.

## Deadline
Sumbit your work before **2016/05/03 (Wed.) 23:59:59**.
