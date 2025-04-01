#!/bin/sh
# wait-for-it.sh

host="$1"
port="$2"
shift 2

until nc -z "$host" "$port"; do
  echo "Esperant que $host:$port estigui llest..."
  sleep 2
done

echo "$host:$port està llest — Executant comanda..."
exec "$@"
