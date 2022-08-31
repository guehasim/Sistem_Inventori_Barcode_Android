<?php
	include "dbConnect.php";
	
	$sub_id 	= $_POST['sub_stuff_id'];
	$problem 	= $_POST['broken_problem'];
	$date 		= $_POST['broken_date'];
	$status 	= $_POST['broken_status'];

//Creating an sql query 
	
	class emp{}
	
	if (empty($sub_id)) { 
		$response = new emp();
		$response->success = 0;
		$response->message = "Kolom isian tidak boleh kosong"; 
		die(json_encode($response));
	} else {

		$query = mysql_query("INSERT INTO invent_broken(broken_id, sub_stuff_id, broken_problem, broken_date, broken_status )
								VALUES (0,'".$sub_id."','".$problem."','".$date."','".$status."')");
		if ($query) {
			$response = new emp();
			$response->success = 1;
			$response->message = "Data berhasil di simpan";
			die(json_encode($response));
		} else{ 
			$response = new emp();
			$response->success = 0;
			$response->message = "Error simpan Data";
			die(json_encode($response)); 
		}	
	}
?>

