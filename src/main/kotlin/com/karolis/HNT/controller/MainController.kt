package com.karolis.HNT.controller

import org.snmp4j.*
import org.snmp4j.mp.SnmpConstants
import org.snmp4j.smi.GenericAddress
import org.snmp4j.smi.OID
import org.snmp4j.smi.OctetString
import org.snmp4j.smi.VariableBinding
import org.snmp4j.transport.DefaultUdpTransportMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MainController(){

    @RequestMapping(
            method = arrayOf(RequestMethod.GET),
            path = arrayOf("/API/TEST")
    )
    @ResponseBody
    fun testSnmp4j(): PDU {
        val transport = DefaultUdpTransportMapping()
        val snmp = Snmp(transport)
        snmp.listen()

        val target = CommunityTarget()
        target.community = OctetString("public")
        target.address = GenericAddress.parse("udp:127.0.0.1/161")
        target.retries = 2
        target.timeout = 1500
        target.version = SnmpConstants.version2c

        val pdu = PDU()
        pdu.type = PDU.GET
        pdu.add(VariableBinding(OID("1.3.6.1.2.1.1.1")))

        val responseEvent = snmp.send(pdu, target)
        val response = responseEvent.response
        if (response == null) {
            System.err.println("response null - error:${responseEvent.error} peerAddress:${responseEvent.peerAddress} source:${responseEvent.source} request:${responseEvent.request}")
        }

        return response


    }
}