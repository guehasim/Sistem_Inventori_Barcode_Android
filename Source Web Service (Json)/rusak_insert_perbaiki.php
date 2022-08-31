<?php
	include "dbConnect.php";
	
	$start_date	= $_POST['repair_start_date'];
	$done_date	= 'NULL';
	$id			= $_POST['broken_id'];
	$repairer	= $_POST['repair_repairer'];
	$condition 	= "";
	$note 		= "";
//Creating an sql query 
	
	class emp{}
	
	if (empty($id)) { 
		$response = new emp();
		$response->success = 0;
		$response->message = "Kolom isian tidak boleh kosong"; 
		die(json_encode($response));
	} else {

		$query = mysql_query("INSERT INTO invent_repair(repair_id,repair_start_date,repair_done_date,broken_id,repair_repairer,repair_condition_result,repair_note)
		 VALUES (0,'".$start_date."','".$done_date."','".$id."','".$repairer."','".$condition."','".$note."')");

		
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

