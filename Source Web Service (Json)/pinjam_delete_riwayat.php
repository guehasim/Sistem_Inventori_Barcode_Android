<?php
	include "dbConnect.php";
	
	$id 	= $_POST['id'];	
		
		class emp{}
		
	if (empty($id)) { 
		$response = new emp();
		$response->success = 0;
		$response->message = "Error hapus Data"; 
		die(json_encode($response));
	} else {
		$query  = mysql_query("DELETE FROM invent_borrow WHERE borrow_id='".$id."' ");
		$querys = mysql_query("DELETE FROM invent_borrow_detail WHERE borrow_id='".$id."' ");
		
		if ($query || $querys) {
			$response = new emp();
			$response->success = 1;
			$response->message = "Data berhasil di hapus";
			die(json_encode($response));
		} else{ 
			$response = new emp();
			$response->success = 0;
			$response->message = "Error hapus Data";
			die(json_encode($response)); 
		}	
	}
?>