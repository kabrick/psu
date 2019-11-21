<?php
include 'dbconfig.php';

$id = $_GET['id'];

$sql = "SELECT id, title, message FROM psu_device_id_messages WHERE device_id = '$id' AND status = '0'";

$result = $conn->query($sql);
$arr = [];
$counter = 0;

if ($result->num_rows > 0) {
	while($row = $result->fetch_assoc()) {
		$arr[$counter]['title'] = $row['title'];
		$arr[$counter]['message'] = $row['message'];
		
		$message_id = $row['id'];
		$results = $conn->query("UPDATE psu_device_id_messages SET status='1' WHERE id = '$message_id'");

 		$counter++;
 	}
}

echo json_encode($arr);

?>