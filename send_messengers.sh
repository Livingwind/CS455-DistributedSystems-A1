test_home=${PWD}
for i in `cat machine_list`
do
echo 'logging into '${i}
gnome-terminal -x bash -c "ssh -t ${i} 'cd ${test_home};
./start_messaging.sh 129.82.44.159 33000;bash;'" &
done
