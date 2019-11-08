<?php
include 'dbconfig.php';

$user_id = $_GET['user_id'];
$device_id = $_GET['device_id'];
$user_category = $_GET['user_category'];

// check if the user_id is already registered
$sql = "SELECT * FROM psu_device_ids WHERE psu_id = '$user_id'";

$result = $conn->query($sql);

if ($result->num_rows < 1){
	// create new record
	$sql = "INSERT INTO psu_device_ids (psu_id, device_id, user_category, last_seen) VALUES ('$user_id','$device_id','$user_category','$device_id')";

	if ($conn->query($sql) === TRUE) {
		echo "1";
	} else {
	    echo "0";
	}
} else {
	// update record and delete previous messages
	$result_assoc = $result->fetch_assoc();
	$id = $result_assoc['id'];
	$current_device_id = $result_assoc['device_id'];

	$sql_update_1 = "UPDATE psu_device_id_messages SET device_id='$device_id' WHERE device_id = '$current_device_id'";

	$sql_update_2 = "UPDATE psu_device_ids SET device_id='$device_id', last_seen='$device_id' WHERE id = '$id'";

	if ($conn->query($sql_update_1) === TRUE && $conn->query($sql_update_2) === TRUE) {
		echo "1";
	} else {
	    echo "0";
	}
}

?>