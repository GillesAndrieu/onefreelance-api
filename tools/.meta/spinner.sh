pid=$!
delay=0.25
spinstr='..'
while [ "$(ps a | awk '{print $1}' | grep $pid)" ]; do
    temp=${spinstr#?}
    printf "%s  " "$spinstr"
    spinstr=$temp${spinstr%"$temp"}
    printf "\b\b\b"
    sleep $delay
done
printf "    \b\b\b\b"
