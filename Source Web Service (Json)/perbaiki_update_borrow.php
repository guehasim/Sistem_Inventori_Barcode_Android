<?php
	include "dbConnect.php";
	
	$sub_stuff_id	= $_POST['sub_stuff_id'];
	$sedia			= $_POST['sedia'];

	class emp{}
	
	if (empty($sub_stuff_id)) { 
		$response = new emp();
		$response->success = 0;
		$response->message = "Kolom isian tidak boleh kosong"; 
		die(json_encode($response));
	} else {
		$query = mysql_query("UPDATE invent_sub_stuff SET sub_stuff_borrow='".$sedia."' WHERE sub_stuff_id='".$sub_stuff_id."' ");

		if ($query) {
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