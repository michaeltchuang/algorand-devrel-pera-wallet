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
//  NotificationAsset.swift

import Foundation
import MagpieCore
import MacaroonUtils

final class NotificationAsset: ALGAPIModel {
    let id: Int64?
    let name: String?
    let code: String?
    let url: String?
    let fractionDecimals: Int?

    init() {
        self.id = nil
        self.name = nil
        self.code = nil
        self.url = nil
        self.fractionDecimals = nil
    }
}

extension NotificationAsset {
    private enum CodingKeys:
        String,
        CodingKey {
        case id = "asset_id"
        case name = "asset_name"
        case code = "unit_name"
        case url
        case fractionDecimals = "fraction_decimals"
    }
}
