echo test started
for i in {1..8}
do
echo testing with $i threads
for j in {1..20}
do
java -classpath "bin" tests.LoadingTest $i
done
done