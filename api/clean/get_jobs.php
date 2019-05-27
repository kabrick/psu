<?php
include 'dbconfig.php';

$sql = "SELECT id, source, title, author_id, timestamp FROM psu_jobs ORDER BY id DESC";

$arr = [];
$counter = 0;

$result = $conn->query($sql);

if ($result->num_rows > 0) {
	while($row = $result->fetch_assoc()) {
		// get user name and photo using the psu_id

		$id = $row['author_id'];

		//get the author full name
		$sql1 = "SELECT name, photo FROM psu_admin WHERE psu_id = '$id'";

		$result1 = $conn->query($sql1);
		$result_assoc1 = $result1->fetch_assoc();

		$arr[$counter]['id'] = $row['id'];
		$arr[$counter]['title'] = $row['title'];
		$arr[$counter]['source'] = $row['source'];
		$arr[$counter]['timestamp'] = $row['timestamp'];
		$arr[$counter]['author'] = $result_assoc1['name'];
		$arr[$counter]['photo'] = $result_assoc1['photo'];

		$counter++;
 	}
} else {
	//
}

echo json_encode($arr);

?>