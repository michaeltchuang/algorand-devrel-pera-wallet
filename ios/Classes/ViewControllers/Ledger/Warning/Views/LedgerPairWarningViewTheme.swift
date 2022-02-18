// Copyright 2022 Pera Wallet, LDA

// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at

//    http://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

//
//   LedgerPairWarningViewTheme.swift

import Foundation
import MacaroonUIKit
import UIKit

struct LedgerPairWarningViewTheme: StyleSheet, LayoutSheet {
    let backgroundColor: Color
    let image: ImageStyle
    let title: TextStyle
    let subtitle: TextStyle
    let actionButton: ButtonStyle
    let actionButtonContentEdgeInsets: LayoutPaddings
    let actionButtonCorner: Corner
    let largerInstuctionViewTheme: LargerInstructionItemViewTheme

    let bottomInset: LayoutMetric
    let horizontalInset: LayoutMetric
    let topInset: LayoutMetric
    let descriptionTopInset: LayoutMetric
    let titleTopInset: LayoutMetric
    let buttonTopInset: LayoutMetric
    let instructionVerticalStackViewTopPadding: LayoutMetric
    let instructionSpacing: LayoutMetric

    init(_ family: LayoutFamily) {
        self.backgroundColor = AppColors.Shared.System.background
        self.image = [
            .image("icon-info-red")
        ]
        self.title = [
            .text(Self.getTitle()),
            .textColor(AppColors.Components.Text.main),
            .textOverflow(FittingText()),
        ]
        self.subtitle = [
            .text(Self.getSubtitle()),
            .textColor(AppColors.Components.Text.gray),
            .textOverflow(FittingText()),
        ]
        self.actionButton = [
            .title("title-close".localized),
            .titleColor([ .normal(AppColors.Components.Button.Secondary.text) ]),
            .font(Fonts.DMSans.medium.make(15)),
            .backgroundColor(AppColors.Components.Button.Secondary.background)
        ]
        self.largerInstuctionViewTheme = LargerInstructionItemViewTheme()
        self.actionButtonContentEdgeInsets = (14, 0, 14, 0)
        self.actionButtonCorner = Corner(radius: 4)

        self.buttonTopInset = 32
        self.horizontalInset = 24
        self.topInset = 32
        self.titleTopInset = 20
        self.descriptionTopInset = 12
        self.bottomInset = 16
        self.instructionVerticalStackViewTopPadding = 40
        self.instructionSpacing = 28
    }
}

extension LedgerPairWarningViewTheme {
    private static func getTitle() -> EditText {
        let font = Fonts.DMSans.medium.make(19)
        let lineHeightMultiplier = 1.13

        return .attributedString(
            "ledger-pairing-first-warning-title"
                .localized
                .attributed([
                    .font(font),
                    .lineHeightMultiplier(lineHeightMultiplier, font),
                    .paragraph([
                        .textAlignment(.center),
                        .lineBreakMode(.byWordWrapping),
                        .lineHeightMultiple(lineHeightMultiplier)
                    ])
                ])
        )
    }

    private static func getSubtitle()  -> EditText {
        let font = Fonts.DMSans.regular.make(15)
        let lineHeightMultiplier = 1.23

        return .attributedString(
            "ledger-pairing-first-warning-description"
                .localized
                .attributed([
                    .font(font),
                    .lineHeightMultiplier(lineHeightMultiplier, font),
                    .paragraph([
                        .textAlignment(.center),
                        .lineBreakMode(.byWordWrapping),
                        .lineHeightMultiple(lineHeightMultiplier)
                    ]),
                ])
        )
    }
}
