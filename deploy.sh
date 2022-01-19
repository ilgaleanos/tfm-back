#!/bin/bash

# gcloud init

read -p "Version? : " VERSION_KDD

gcloud auth configure-docker
export RUTA_KDD="Dockerfile"
export ZONA_KDD="us-east1-d"
export NOMBRE_KDD="clienteunicointegracion"
export PROJECT_KDD="lucia-stg"

# creamos las imagenes
# gradle jibDockerBuild

# enviamos
docker push gcr.io/${PROJECT_KDD}/${NOMBRE_KDD}:${VERSION_KDD}
