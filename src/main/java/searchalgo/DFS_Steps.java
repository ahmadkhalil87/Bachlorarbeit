package searchalgo;

public enum DFS_Steps {
	Fail,
	Goal_reached,
	PUSH_Start_Problem_in_OPEN,
	LOOP_start,
	IF_OPEN_empty,
	IF_OPEN_Empty_true,
	return_fail,
	IF_OPEN_Empty_end,
	POP_OPEN_n,
	PUSH_n_to_CLOSED,
	IF_Depth_reached,
	IF_Depth_reached_true,
	cleanup_closed_depth,
	IF_Depth_reached_false,
	For_Loop,
	set_Backpointer,
	is_Goal_reached,
	is_Goal_reached_true,
	return_goal,
	is_Goal_reached_end,
	is_n1_Deadend,
	is_n1_Deadend_true,
	cleanup_closed_deadend,
	is_n1_Deadend_false,
	PUSH_n1_to_OPEN,
	is_n1_Deadend_end,
	For_Loop_end,
	IF_n_has_no_Successors,
	IF_n_has_no_successors_true,
	cleanup_closed_no_successors,
	IF_Depth_reached_end,
	LOOP_end { //32
        @Override
        public DFS_Steps next() {
            return LOOP_start;
        };
    };
	public DFS_Steps next() {
        return values()[ordinal() + 1];
    }
	
}
