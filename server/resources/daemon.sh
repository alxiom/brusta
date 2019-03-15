#!/bin/bash
# Uses a PID file to add daemon-like behavior to an arbitrary program.
################################################################################
#./daemon.sh run.sh start
################################################################################

usage() {
  echo "Usage: `basename $0` PROGRAM {start|stop|restart|force-stop|force-restart|status} [PIDFILE|PID]" >&2
  echo "Where: PROGRAM is an executable file." >&2
  echo "       PIDFILE is the file that contains (or will contain) the PID." >&2
  echo "       PID is a process id to use in place of a PIDFILE." >&2
}

# At least two arguments are required.
if [[ -z "${1}" || -z "${2}" ]]; then
  usage
  exit 1
fi

# The first argument must be an actual file.
if [[ ! -e "${1}" ]]; then
  echo "File \"${1}\" not found. Exiting." 1>&2;
  exit 2
fi

PROGLONG=$(realpath $1)
PROGSHORT=$(basename ${PROGLONG})
PIDFILE=${PIDFILE:-"${PROGSHORT}.pid"}

# If there is a third argument, try to interpret it as a file or PID value.
if [[ ${3} ]]; then
  if [[ `expr $3 + 1 2> /dev/null` ]]; then
    PID=$3;
  elif [[ -e ${3} || "${2}" == "start" ]]; then
    PIDFILE="${3}"
  else
    echo "Third argument must be a number or a file. (Found $3). Exiting." 1>&2;
    exit 3
  fi
fi

# Get the PID from PIDFILE if we don't have one yet.
if [[ -z "${PID}" && -e ${PIDFILE} ]]; then
  PID=$(cat ${PIDFILE});
fi

function proc2cmd {
    declare PID=$1
    declare CMDLINE=$(echo $(cat /proc/$PID/cmdline | while read -r -d $'\0' LINE; do echo $LINE; done | tail -1))
    realpath "${CMDLINE}"
}

start() {
  echo "Starting $PROGSHORT (PID written to $PIDFILE)."
  nohup ${PROGLONG} &>> ${PROGSHORT}.out & # Under bash 4 does not support "&>>" syntax
  echo $! > ${PIDFILE}
}

status() {
  if [[ -z "${PID}" ]]; then
    echo "${PROGSHORT} is not running (missing PID)."
  else
    echo "${PROGSHORT} is running (PID: ${PID})."
  fi
}

stop() {
  if [[ -z "${PID}" ]]; then
    echo "${PROGSHORT} is not running (missing PID)."
  else
    pkill -P ${PID}
    kill ${PID}
    echo kill ${PID}
    if [[ -n "${PIDFILE}" ]]; then
      rm ${PIDFILE}
    fi
  fi
}

case "$2" in
  start)
        start;
        ;;
  restart)
        stop; sleep 1; start;
        ;;
  stop)
        stop
        ;;
  force-stop)
        stop -9
        ;;
  force-restart)
        stop -9; sleep 1; start;
        ;;
  status)
        status
        ;;
  *)
        usage
        exit 4
        ;;
esac

exit 0

######################################################################
# This program is free software. It comes without any warranty, to
# the extent permitted by applicable law.
#
# If your jurisdiction supports the concept of Public Domain works,
# this program is released into the Public Domain.
#
# Otherwise this program is available under the following terms:
#---------------------------------------------------------------------
# Copyright (c) 2012, Rodney Waldhoff
#
# Everyone is permitted to copy and distribute verbatim or modified
# copies of this program with or without this notice.
######################################################################
