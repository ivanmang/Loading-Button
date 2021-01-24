package com.udacity


sealed class ButtonState {
    object Clicked : ButtonState()
    object Loading : ButtonState()
    object Completed : ButtonState()
}

enum class Status(val value:String)  {SUCCESS("Success"), FAIL("Fail")}