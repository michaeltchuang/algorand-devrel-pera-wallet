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
//  NSAttributedString+Additions.swift

import Foundation
import CoreGraphics

extension NSAttributedString {
    static func + (lhs: NSAttributedString, rhs: NSAttributedString) -> NSAttributedString {
        let compoundAttributedString = NSMutableAttributedString()
        
        compoundAttributedString.append(lhs)
        compoundAttributedString.append(rhs)
        
        return compoundAttributedString
    }

    func height(withConstrained width: CGFloat) -> CGFloat {
        let constraintRect = CGSize(width: width, height: .greatestFiniteMagnitude)

        let rect = self.boundingRect(
            with: constraintRect,
            options: [.usesLineFragmentOrigin],
            context: nil
        )

        return ceil(rect.size.height)
    }
}
