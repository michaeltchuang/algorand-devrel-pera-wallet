
import com.algorand.algosdk.crypto.Address
import java.math.BigDecimal
import java.math.BigInteger
import java.nio.ByteBuffer
import java.nio.ByteOrder

const val EIGHT: Int = 8
const val MAX_LENGTH = 35
const val TRUNCATE_START = 5
const val TRUNCATE_END = 6
const val TEN_K = 10000L
const val ALGOS_TO_MICROALGOS_RATIO = 1000000L
const val TWO_HUNDRED_FORTY = 240

fun truncatedAlgorandAddress(str: String): String {
    if (str.endsWith(".algo")) {
        return str
    } else if (str.length > MAX_LENGTH) {
        return "${str.substring(0,TRUNCATE_START)} ... ${str.substring(str.length - TRUNCATE_END)}"
    } else {
        return "empty"
    }
}

fun getValidatorListBoxName(id: Long): ByteArray {
    val prefix = byteArrayOf('v'.toByte())
    val ibytes = ByteBuffer.allocate(EIGHT).order(ByteOrder.BIG_ENDIAN).putLong(id).array()
    return prefix + ibytes
}

fun getStakerPoolSetBoxName(stakerAccount: Address): ByteArray {
    val prefix = "sps".toByteArray()
    val combinedArray = prefix + stakerAccount.bytes
    return combinedArray
}

fun getStakerLedgerBoxName(): ByteArray {
    return "stakers".toByteArray()
}

fun calculateRewardBlock(
    currentRound: Long,
    lastPayout: Long,
    epochRoundLength: Long
): Long {
    val startOfEpoch = lastPayout - lastPayout.mod(epochRoundLength)
    val rewardPayout = startOfEpoch + epochRoundLength
    return if (rewardPayout >= currentRound && currentRound != 0L) {
        rewardPayout
    } else {
        0L
    }
}

fun Double.toMicroFromAlgo(): Double {
    return this.times(ALGOS_TO_MICROALGOS_RATIO)
}

fun Double.toAlgoFromMicro(): Double {
    return this.div(ALGOS_TO_MICROALGOS_RATIO)
}

fun Long.toMicroFromAlgo(): Long {
    return this.times(ALGOS_TO_MICROALGOS_RATIO)
}

fun Long.toAlgoFromMicro(): Long {
    return this.div(ALGOS_TO_MICROALGOS_RATIO)
}

fun BigInteger.toMicroFromAlgo(): BigInteger {
    return this.multiply(ALGOS_TO_MICROALGOS_RATIO.toBigInteger())
}

fun BigInteger.toAlgoFromMicro(): BigInteger {
    return this.div(ALGOS_TO_MICROALGOS_RATIO.toBigInteger())
}

fun BigDecimal.toMicroFromAlgo(): BigDecimal {
    return this.multiply(ALGOS_TO_MICROALGOS_RATIO.toBigDecimal())
}

fun BigDecimal.toAlgoFromMicro(): BigDecimal {
    return this.div(ALGOS_TO_MICROALGOS_RATIO.toBigDecimal())
}
