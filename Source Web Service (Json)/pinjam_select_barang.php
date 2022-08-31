<?php
	include "dbConnect.php";
	
	$id 	= $_POST['id'];
	
	class emp{}
	
	if (empty($id)) { 
		echo $id;
		$response = new emp();
		$response->success = 0;
		$response->message = "Error Mengambil Data"; 
		die(json_encode($response));
	} else {
		$query 	= mysql_query("SELECT iii.sub_stuff_serial_number, iiii.stuff_name
								FROM invent_borrow i
								INNER JOIN invent_borrow_detail ii ON i.borrow_id = ii.borrow_id
								INNER JOIN invent_sub_stuff iii ON ii.sub_stuff_id = iii.sub_stuff_id
								INNER JOIN invent_stuff iiii ON iii.stuff_id = iiii.stuff_id
								WHERE i.borrow_borrower ='".$id."' ");

		$json = array();

		while ($row = mysql_fetch_assoc($query)) {
			$json[] = $row;
		}
		echo json_encode($json);
		mysql_close($connect);

	}
?>