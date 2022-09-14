package searchalgo;

public enum BFS_Steps {
	Fail,
	Goal_reached,
	Enqueue_Start_Problem_in_OPEN,
	LOOP_start,
	IF_OPEN_empty,
	IF_OPEN_empty_true,
	return_fail,
	IF_OPEN_empty_end,
	Dequeue_OPEN_n,
	PUSH_n_to_CLOSED,
	FOR_Loop,
	set_Backpointer,
	is_Goal_reached,
	is_Goal_reached_true,
	return_goal,
	is_Goal_reached_end,
	is_n1_Deadend,
	is_n1_Deadend_true,
	cleanup_closed_deadend,
	is_n1_Deadend_false,
	Enqueue_n1_to_OPEN,
	is_n1_Deadend_end,
	FOR_Loop_end,
	LOOP_end {
        @Override
        public BFS_Steps next() {
            return LOOP_start;
        };
    };
	public BFS_Steps next() {
        return values()[ordinal() + 1];
    }
	
}
