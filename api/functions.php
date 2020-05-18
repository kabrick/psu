<?php

function send_push_notification($title, $message, $user_category) {

	include 'dbconfig.php';

	if ($user_category != 0) {
		$device_ids = $conn->query("SELECT * FROM psu_device_ids WHERE user_category = '$user_category'");
	} else {
		$device_ids = $conn->query("SELECT * FROM psu_device_ids");
	}

	if ($device_ids->num_rows > 0) {
		while($row = $device_ids->fetch_assoc()) {
			$device_id = $row['device_id'];
			$conn->query("INSERT INTO psu_device_id_messages (device_id, title, message) VALUES ('$device_id','$title','$message')");
		}
	}

}

function send_single_push_notification($title, $message, $psu_id) {

	include 'dbconfig.php';

	$device_ids = $conn->query("SELECT * FROM psu_device_ids WHERE psu_id = '$psu_id'");

	if ($device_ids->num_rows > 0) {
		while($row = $device_ids->fetch_assoc()) {
			$device_id = $row['device_id'];
			$conn->query("INSERT INTO psu_device_id_messages (device_id, title, message) VALUES ('$device_id','$title','$message')");
		}
	}

}

function send_admin_notifications_news($news_title) {

	include 'dbconfig.php';

	$admins = $conn->query("SELECT name,email FROM psu_admin WHERE type = 'admin'");

	if ($admins->num_rows > 0) {
		while($row = $admins->fetch_assoc()) {
			$subject = 'PSU Approval Needed';
			$message = "Hello " . $row['name'] . "\n" . "A news post with the title " . $news_title . " has been posted and requires your approval." . "\n" . "You are receiving this email because you are an admin for the PSU Practice App.";
			$headers = 'From: PSU APP<psuapp2020@gmail.com>';

			mail($row['email'],$subject,$message,$headers);
		}
	}

}

function send_admin_notifications_jobs($jobs_title) {

	include 'dbconfig.php';

	$admins = $conn->query("SELECT name,email FROM psu_admin WHERE type = 'admin'");

	if ($admins->num_rows > 0) {
		while($row = $admins->fetch_assoc()) {
			$subject = 'PSU Approval Needed';
			$message = "Hello " . $row['name'] . "\n" . "A job post with the title " . $jobs_title . " has been posted and requires your approval." . "\n" . "You are receiving this email because you are an admin for the PSU Practice App.";
			$headers = 'From:  PSU APP<psuapp2020@gmail.com>';

			mail($row['email'],$subject,$message,$headers);
		}
	}

}

function send_email_global($title, $text) {

	include 'dbconfig.php';

	$admins = $conn->query("SELECT name,email FROM psu_admin");

	if ($admins->num_rows > 0) {
		while($row = $admins->fetch_assoc()) {
			$subject = 'PSU News: ' . $title;
			$message = $text;
			$headers = 'From:  PSU APP<psuapp2020@gmail.com>';

			mail($row['email'],$subject,$message,$headers);
		}
	}

}

?>