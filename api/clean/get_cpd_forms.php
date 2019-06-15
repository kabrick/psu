<?php
include 'dbconfig.php';

$sql = "SELECT * FROM psu_ecpd ORDER BY id DESC";

$arr = [];
$counter = 0;

$result = $conn->query($sql);

if ($result->num_rows > 0) {
	while($row = $result->fetch_assoc()) {
		$id = $row['author_id'];

		//get the author full name
		$sql1 = "SELECT name FROM psu_admin WHERE psu_id = '$id'";

		$result1 = $conn->query($sql1);
		$result_assoc1 = $result1->fetch_assoc();

		$arr[$counter]['id'] = $row['id'];
		$arr[$counter]['title'] = $row['title'];
		$arr[$counter]['description'] = $row['description'];
		$arr[$counter]['timestamp'] = $row['timestamp'];
		$arr[$counter]['author'] = $result_assoc1['name'];

		$counter++;
 	}
} else {
	//
}

echo json_encode($arr);

?>