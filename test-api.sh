#!/usr/bin/env bash
set -euo pipefail

curl "http://localhost:8585/api/orders/data?x=10&y=20&mainValue=ABC"
echo
curl "http://localhost:8585/api/orders/calculate?price=200&quantity=3"
echo
curl "http://localhost:8585/api/orders/notify?orderId=100"
echo
curl "http://localhost:8585/api/orders/plain?value=ABC"
echo
