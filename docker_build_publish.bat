docker build -t git-backend .
docker image rm moiiii/git-backend
docker tag git-backend moiiii/git-backend
docker push moiiii/git-backend