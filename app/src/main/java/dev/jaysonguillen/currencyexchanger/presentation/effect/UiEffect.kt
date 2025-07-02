package dev.jaysonguillen.currencyexchanger.presentation.effect

sealed class UiEffect {
    data object ShowSuccessExchangeDialog : UiEffect()
}