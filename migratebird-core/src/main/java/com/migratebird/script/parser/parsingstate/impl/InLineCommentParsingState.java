/**
 * Copyright 2014 Turgay Kivrak
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.migratebird.script.parser.parsingstate.impl;

import static com.migratebird.util.CharacterUtils.isNewLineCharacter;

import com.migratebird.script.parser.impl.HandleNextCharacterResult;
import com.migratebird.script.parser.impl.StatementBuilder;
import com.migratebird.script.parser.parsingstate.ParsingState;


/**
 * A state for parsing an in-line comment (-- comment) part of a script.
 *
*/
public class InLineCommentParsingState implements ParsingState {

    protected HandleNextCharacterResult stayInLineCommentResult, backToNormalResult;


    /**
     * Initializes the state with the normal parsing state, that should be returned when the comment end is reached..
     *
     * @param normalParsingState The normal state, not null
     */
    public void linkParsingStates(ParsingState normalParsingState) {
        this.stayInLineCommentResult = new HandleNextCharacterResult(this, false);
        this.backToNormalResult = new HandleNextCharacterResult(normalParsingState, false);
    }


    /**
     * Determines whether the end of the line comment is reached.
     * If that is the case, the normal parsing state is returned.
     *
     * @param previousChar     The previous char, null if none
     * @param currentChar      The current char
     * @param nextChar         The next char, null if none
     * @param statementBuilder The statement builder, not null
     * @return The next parsing state, null if the end of the statement is reached
     */
    public HandleNextCharacterResult getNextParsingState(Character previousChar, Character currentChar, Character nextChar, StatementBuilder statementBuilder) {
        if (isNewLineCharacter(currentChar)) {
            return backToNormalResult;
        }
        return stayInLineCommentResult;
    }

}
