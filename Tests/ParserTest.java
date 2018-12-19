package Source.Tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import Source.LogicFiles.Parser;
import org.junit.jupiter.api.Test;


public class ParserTest {
    @Test
    public void testGetWikiSearchResult() {
        String request = "Вода";
        String result = "Вода́ (оксид водорода) — бинарное неорганическое соединение с химической формулой Н2O. Молекула воды состоит из двух атомов водорода и одного — кислорода, которые соединены между собой ковалентной связью. При нормальных условиях представляет собой прозрачную жидкость, не имеющую цвета (при малой толщине слоя), запаха и вкуса. В твёрдом состоянии называется льдом (кристаллы льда могут образовывать снег или иней), а в газообразном — водяным паром. Вода также может существовать в виде жидких кристаллов (на гидрофильных поверхностях)[5][6]. https://ru.wikipedia.org/wiki/%D0%92%D0%BE%D0%B4%D0%B0";
        assertEquals(result, Parser.getWikiSearchResult(request));
    }

    @Test
    public void testDecorate() {
        String text = "РРРР. ЛАЛАЛАЛА. https://блабла. Переходим";
        String result = "РРРР.<i> ЛАЛАЛАЛА. </i>\n<a href= \"https://блабла. \">Читать далее</a>\nПереходим";
        assertEquals(result, Parser.decorate(text));

    }
}
