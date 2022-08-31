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
		$query  = mysql_query("DELETE i,ii FROM invent_repair i 
									INNER JOIN invent_broken ii ON i.broken_id = ii.broken_id WHERE i.repair_id='".$id."' ");
		
		if ($query) {
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