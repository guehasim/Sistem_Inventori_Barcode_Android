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
		$query  = mysql_query("DELETE i,ii FROM invent_sub_stuff i 
								INNER JOIN invent_stuff ii ON i.stuff_id = ii.stuff_id 
								WHERE i.sub_stuff_id='".$id."' ");
		
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