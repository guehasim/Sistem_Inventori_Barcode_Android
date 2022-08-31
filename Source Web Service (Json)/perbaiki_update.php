<?php
	include "dbConnect.php";
	
	$repair_id		= $_POST['repair_id'];
	$done_date 		= $_POST['done_date'];
	$note			= $_POST['catatan'];
	$sub_stuff_id	= $_POST['sub_stuff_id'];
	$kondisi		= $_POST['kondisi'];
	$borrow 		= "1";
	$broken_id		= $_POST['broken_id'];
	$status 		= "3";

	class emp{}
	
	if (empty($repair_id)) { 
		$response = new emp();
		$response->success = 0;
		$response->message = "Kolom isian tidak boleh kosong"; 
		die(json_encode($response));
	} else {

		$query = mysql_query("UPDATE invent_sub_stuff SET sub_stuff_condition='".$kondisi."', sub_stuff_borrow= '".$borrow."' WHERE sub_stuff_id='".$sub_stuff_id."' ");

		$querys = mysql_query("UPDATE invent_repair SET repair_done_date='".$done_date."', repair_condition_result='".$kondisi."',
				repair_note='".$note."'  WHERE repair_id='".$repair_id."' ");

		$queryt = mysql_query("UPDATE invent_broken SET broken_status='".$status."' WHERE broken_id='".$broken_id."' ");
		
		if ($query && $querys && $queryt) {
			$response = new emp();
			$response->success = 1;
			$response->message = "Data berhasil di update";
			die(json_encode($response));
		} else{ 
			$response = new emp();
			$response->success = 0;
			$response->message = "Error update Data";
			die(json_encode($response)); 
		}	
	}
?>