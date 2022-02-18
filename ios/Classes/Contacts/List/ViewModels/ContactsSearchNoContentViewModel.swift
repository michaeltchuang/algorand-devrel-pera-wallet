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
//   ContactsSearchNoContentViewModel.swift

import Foundation
import MacaroonUIKit

struct ContactsSearchNoContentViewModel: NoContentViewModel {
    private(set) var icon: Image?
    private(set) var title: EditText?
    private(set) var body: EditText?

    init() {
        bindTitle()
        bindBody()
    }
}

extension ContactsSearchNoContentViewModel {
    private mutating func bindTitle() {
        title = .string("contact-search-empty-title".localized)
    }

    private mutating func bindBody() {
        body = .string("contact-search-empty-detail".localized)
    }
}
