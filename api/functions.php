<?php

function send_push_notification($title, $message, $user_category) {
	if ($user_category != 0) {
		$device_ids = $conn->query("SELECT * FROM psu_device_ids WHERE user_category = '$user_category'");
	} else {
		$device_ids = $conn->query("SELECT * FROM psu_device_ids");
	}

	if ($device_ids->num_rows > 0) {
		while($row = $device_ids->fetch_assoc()) {
			$sql = "INSERT INTO psu_device_id_messages (device_id, title, message) VALUES ('$device_id','$title','$message')";
		}
	}
}

?>