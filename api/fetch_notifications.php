<?php
include 'dbconfig.php';

$id = $_GET['id'];

$sql = "SELECT id, title, message FROM psu_device_id_messages WHERE device_id = '$id' AND status = '0'";

$result = $conn->query($sql);

$counter = 0;

if ($result->num_rows > 0) {
	while($row[] = $result->fetch_assoc()) {
		$message_id = $row[$counter]['id'];
		$results = $conn->query("UPDATE psu_device_id_messages SET status='1' WHERE id = '$message_id'");
		$counter++;

 		$json = json_encode($row);
 	}
}

echo $json;

?>