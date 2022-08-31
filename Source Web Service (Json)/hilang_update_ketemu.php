<?php
	include "dbConnect.php";
	
	$sub_stuff_id	= $_POST['sub_stuff_id'];
	$broken_id		= $_POST['broken_id'];
	$kondisi		= "1";
	$status 		= "4";

	class emp{}
	
	if (empty($sub_stuff_id)) { 
		$response = new emp();
		$response->success = 0;
		$response->message = "Kolom isian tidak boleh kosong"; 
		die(json_encode($response));
	} else {

		$query = mysql_query("UPDATE invent_sub_stuff SET sub_stuff_borrow='".$kondisi."' WHERE sub_stuff_id='".$sub_stuff_id."' ");

		$query = mysql_query("UPDATE invent_broken SET broken_status='".$status."' WHERE broken_id='".$broken_id."' ");
		
		if ($query) {
			$response = new emp();
			$response->success = 1;
			$response->message = "Inventaris Sudah Ditemukan";
			die(json_encode($response));
		} else{ 
			$response = new emp();
			$response->success = 0;
			$response->message = "Error Ditemukan";
			die(json_encode($response)); 
		}	
	}
?>