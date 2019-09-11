<?php
include 'dbconfig.php';

$sql = "SELECT * FROM psu_forums WHERE approved = '0' ORDER BY id DESC";

$arr = array();
$counter = 0;

$result = $conn->query($sql);

if ($result->num_rows > 0) {
	while($row = $result->fetch_assoc()) {
		// get author name
		$id = $row['author'];

		$sql1 = "SELECT name, photo FROM psu_admin WHERE psu_id = '$id'";

		$result1 = $conn->query($sql1);
		$result_assoc1 = $result1->fetch_assoc();

		$arr[$counter]['name'] = $result_assoc1['name'];
		$arr[$counter]['photo'] = $result_assoc1['photo'];

		// get moderator name
		$id = $row['moderator'];

		$sql1 = "SELECT name FROM psu_admin WHERE psu_id = '$id'";

		$result1 = $conn->query($sql1);
		$result_assoc1 = $result1->fetch_assoc();

		$arr[$counter]['moderator'] = $result_assoc1['name'];

		$arr[$counter]['id'] = $row['id'];
		$arr[$counter]['title'] = $row['title'];
		$arr[$counter]['picture_url'] = $row['picture_url'];
		$arr[$counter]['document_url'] = $row['document_url'];
		$arr[$counter]['document_name'] = $row['document_name'];
		$arr[$counter]['target_audience'] = $row['target_audience'];
		$arr[$counter]['timestamp'] = $row['created_at'];

		$counter++;
 	}
} else {
	//
}

echo json_encode($arr);

?>