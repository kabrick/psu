<?php
include 'dbconfig.php';

$category = $_GET['id'];

$sql = "SELECT * FROM psu_forums WHERE approved = '1' AND target_audience = '0' OR target_audience = '$category' ORDER BY id DESC";

$arr = array();
$counter = 0;

$result = $conn->query($sql);

if ($result->num_rows > 0) {
	while($row = $result->fetch_assoc()) {
		$id = $row['author'];

		$sql1 = "SELECT name, photo FROM psu_admin WHERE psu_id = '$id'";

		$result1 = $conn->query($sql1);
		$result_assoc1 = $result1->fetch_assoc();

		$arr[$counter]['id'] = $row['id'];
		$arr[$counter]['title'] = $row['title'];
		$arr[$counter]['picture_url'] = $row['picture_url'];
		$arr[$counter]['document_url'] = $row['document_url'];
		$arr[$counter]['document_name'] = $row['document_name'];
		$arr[$counter]['timestamp'] = $row['created_at'];
		$arr[$counter]['name'] = $result_assoc1['name'];
		$arr[$counter]['photo'] = $result_assoc1['photo'];

		$counter++;
 	}
} else {
	//
}

echo json_encode($arr);

?>