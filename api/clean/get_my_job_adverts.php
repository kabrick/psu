<?php
include 'dbconfig.php';

$psu_id = $_GET['psu_id'];

$sql = "SELECT id, title, company_name, deadline FROM psu_jobs WHERE author_id = '$psu_id' ORDER BY id DESC";

$result = $conn->query($sql);

$responce = array();

$count = 0;

while($row = $result->fetch_assoc()) {
	$responce[$count]['company_name'] = $row['company_name'];
	$responce[$count]['deadline'] = $row['deadline'];
	$responce[$count]['id'] = $row['id'];
	$responce[$count]['title'] = $row['title'];

	$count++;
}

echo json_encode($responce);

?>