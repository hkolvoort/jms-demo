#!/bin/bash

# Script om ConfigMap te genereren vanuit broker.xml bestand
# Usage: ./generate-configmap.sh

NAMESPACE="artemis"
CONFIGMAP_NAME="artemis-broker-config"
BROKER_XML_FILE="broker.xml"

# Controleer of broker.xml bestaat
if [ ! -f "$BROKER_XML_FILE" ]; then
    echo "Error: $BROKER_XML_FILE niet gevonden in $(pwd)"
    exit 1
fi

# Genereer ConfigMap met kubectl
kubectl create configmap "$CONFIGMAP_NAME" --from-file="$BROKER_XML_FILE" -n "$NAMESPACE" --dry-run=client -o yaml > "artemis-configmap.yaml"

echo "ConfigMap gegenereerd: artemis-configmap.yaml"
echo ""
echo "Om toe te passen in cluster, voer uit:"
echo "  kubectl apply -f artemis-configmap.yaml"
