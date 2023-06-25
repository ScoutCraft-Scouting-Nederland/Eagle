package nl.scoutcraft.eagle.proxy.utils;

import java.util.HashMap;
import java.util.Map;

public class VersionParser {

    private static final Map<Integer, String> VERSIONS;

    public static String parse(int version) {
        return VERSIONS.getOrDefault(version, String.valueOf(version));
    }

    static {
        VERSIONS = new HashMap<>();
        VERSIONS.put(757, "1.18/1.18.1");
        VERSIONS.put(756, "1.17.1");
        VERSIONS.put(755, "1.17");
        VERSIONS.put(803, "Java Edition Combat Test 8b/8c");
        VERSIONS.put(802, "Java Edition Combat Test 7/7b/7c");
        VERSIONS.put(801, "Java Edition Combat Test 6");
        VERSIONS.put(1073741851, "21w19a - [1.17]");
        VERSIONS.put(1073741850, "21w18a - [1.17]");
        VERSIONS.put(1073741849, "21w17a - [1.17]");
        VERSIONS.put(1073741847, "21w16a - [1.17]");
        VERSIONS.put(1073741846, "21w15a - [1.17]");
        VERSIONS.put(1073741845, "21w14a - [1.17]");
        VERSIONS.put(1073741844, "21w13a - [1.17]");
        VERSIONS.put(1073741843, "21w11a - [1.17]");
        VERSIONS.put(1073741842, "21w10a - [1.17]");
        VERSIONS.put(1073741841, "21w08b - [1.17]");
        VERSIONS.put(1073741840, "21w08a - [1.17]");
        VERSIONS.put(1073741839, "21w07a - [1.17]");
        VERSIONS.put(1073741838, "21w06a - [1.17]");
        VERSIONS.put(1073741837, "21w05b - [1.17]");
        VERSIONS.put(1073741836, "21w05a - [1.17]");
        VERSIONS.put(1073741835, "21w03a - [1.17]");
        VERSIONS.put(754, "1.16.4/1.16.5");
        VERSIONS.put(1073741834, "1.16.5-rc1");
        VERSIONS.put(1073741833, "20w51a - [1.17]");
        VERSIONS.put(1073741832, "20w49a - [1.17]");
        VERSIONS.put(1073741831, "20w48a - [1.17]");
        VERSIONS.put(1073741830, "20w46a - [1.17]");
        VERSIONS.put(1073741829, "20w45a - [1.17]");
        VERSIONS.put(1073741827, "1.16.4-rc1");
        VERSIONS.put(1073741826, "1.16.4-pre2");
        VERSIONS.put(1073741825, "1.16.4-pre1");
        VERSIONS.put(753, "1.16.3");
        VERSIONS.put(752, "1.16.3-rc1");
        VERSIONS.put(751, "1.16.2");
        VERSIONS.put(750, "1.16.2-rc2");
        VERSIONS.put(749, "1.16.2-rc1");
        VERSIONS.put(748, "1.16.2-pre3");
        VERSIONS.put(746, "1.16.2-pre2");
        VERSIONS.put(744, "1.16.2-pre1");
        VERSIONS.put(743, "20w30a - [1.16]");
        VERSIONS.put(741, "20w29a - [1.16]");
        VERSIONS.put(740, "20w28a - [1.16]");
        VERSIONS.put(738, "20w27a - [1.16]");
        VERSIONS.put(736, "1.16.1");
        VERSIONS.put(735, "1.16");
        VERSIONS.put(734, "1.16-rc1");
        VERSIONS.put(733, "1.16-pre8");
        VERSIONS.put(732, "1.16-pre7");
        VERSIONS.put(730, "1.16-pre6");
        VERSIONS.put(729, "1.16-pre5");
        VERSIONS.put(727, "1.16-pre4");
        VERSIONS.put(725, "1.16-pre3");
        VERSIONS.put(722, "1.16-pre2");
        VERSIONS.put(721, "1.16-pre1");
        VERSIONS.put(719, "20w22a - [1.16]");
        VERSIONS.put(718, "20w21a - [1.16]");
        VERSIONS.put(717, "20w20b - [1.16]");
        VERSIONS.put(716, "20w20a - [1.16]");
        VERSIONS.put(715, "20w19a - [1.16]");
        VERSIONS.put(714, "20w18a - [1.16]");
        VERSIONS.put(713, "20w17a - [1.16]");
        VERSIONS.put(712, "20w16a - [1.16]");
        VERSIONS.put(711, "20w15a - [1.16]");
        VERSIONS.put(710, "20w14a - [1.16]");
        VERSIONS.put(709, "20w13b - [1.16]");
        VERSIONS.put(708, "20w13a - [1.16]");
        VERSIONS.put(707, "20w12a - [1.16]");
        VERSIONS.put(706, "20w11a - [1.16]");
        VERSIONS.put(705, "20w10a - [1.16]");
        VERSIONS.put(704, "20w09a - [1.16]");
        VERSIONS.put(703, "20w08a - [1.16]");
        VERSIONS.put(702, "20w07a - [1.16]");
        VERSIONS.put(701, "20w06a - [1.16]");
        VERSIONS.put(578, "1.15.2");
        VERSIONS.put(577, "1.15.2-pre2");
        VERSIONS.put(576, "1.15.2-pre1");
        VERSIONS.put(575, "1.15.1");
        VERSIONS.put(574, "1.15.1-pre1");
        VERSIONS.put(573, "1.15");
        VERSIONS.put(572, "1.15-pre7");
        VERSIONS.put(571, "1.15-pre6");
        VERSIONS.put(570, "1.15-pre5");
        VERSIONS.put(569, "1.15-pre4");
        VERSIONS.put(567, "1.15-pre3");
        VERSIONS.put(566, "1.15-pre2");
        VERSIONS.put(565, "1.15-pre1");
        VERSIONS.put(564, "19w46b - [1.15]");
        VERSIONS.put(563, "19w46a - [1.15]");
        VERSIONS.put(562, "19w45b - [1.15]");
        VERSIONS.put(561, "19w45a - [1.15]");
        VERSIONS.put(560, "19w44a - [1.15]");
        VERSIONS.put(559, "19w42a - [1.15]");
        VERSIONS.put(558, "19w41a - [1.15]");
        VERSIONS.put(557, "19w40a - [1.15]");
        VERSIONS.put(556, "19w39a - [1.15]");
        VERSIONS.put(555, "19w38b - [1.15]");
        VERSIONS.put(554, "19w38a - [1.15]");
        VERSIONS.put(553, "19w37a - [1.15]");
        VERSIONS.put(552, "19w36a - [1.15]");
        VERSIONS.put(551, "19w35a - [1.15]");
        VERSIONS.put(550, "19w34a - [1.15]");
        VERSIONS.put(498, "1.14.4");
        VERSIONS.put(497, "1.14.4-pre7");
        VERSIONS.put(496, "1.14.4-pre6");
        VERSIONS.put(495, "1.14.4-pre5");
        VERSIONS.put(494, "1.14.4-pre4");
        VERSIONS.put(493, "1.14.4-pre3");
        VERSIONS.put(492, "1.14.4-pre2");
        VERSIONS.put(491, "1.14.4-pre1");
        VERSIONS.put(490, "1.14.3");
        VERSIONS.put(489, "1.14.3-pre4");
        VERSIONS.put(488, "1.14.3-pre3");
        VERSIONS.put(487, "1.14.3-pre2");
        VERSIONS.put(486, "1.14.3-pre1");
        VERSIONS.put(485, "1.14.2");
        VERSIONS.put(484, "1.14.2-pre4");
        VERSIONS.put(483, "1.14.2-pre3");
        VERSIONS.put(482, "1.14.2-pre2");
        VERSIONS.put(481, "1.14.2-pre1");
        VERSIONS.put(480, "1.14.1");
        VERSIONS.put(479, "1.14.1-pre2");
        VERSIONS.put(478, "1.14.1-pre1");
        VERSIONS.put(477, "1.14");
        VERSIONS.put(476, "1.14-pre5");
        VERSIONS.put(475, "1.14-pre4");
        VERSIONS.put(474, "1.14-pre3");
        VERSIONS.put(473, "1.14-pre2");
        VERSIONS.put(472, "1.14-pre1");
        VERSIONS.put(471, "19w14b - [1.14]");
        VERSIONS.put(470, "19w14a - [1.14]");
        VERSIONS.put(469, "19w13b - [1.14]");
        VERSIONS.put(468, "19w13a - [1.14]");
        VERSIONS.put(467, "19w12b - [1.14]");
        VERSIONS.put(466, "19w12a - [1.14]");
        VERSIONS.put(465, "19w11b - [1.14]");
        VERSIONS.put(464, "19w11a - [1.14]");
        VERSIONS.put(463, "19w09a - [1.14]");
        VERSIONS.put(462, "19w08b - [1.14]");
        VERSIONS.put(461, "19w08a - [1.14]");
        VERSIONS.put(460, "19w07a - [1.14]");
        VERSIONS.put(459, "19w06a - [1.14]");
        VERSIONS.put(458, "19w05a - [1.14]");
        VERSIONS.put(457, "19w04b - [1.14]");
        VERSIONS.put(456, "19w04a - [1.14]");
        VERSIONS.put(455, "19w03c - [1.14]");
        VERSIONS.put(454, "19w03b - [1.14]");
        VERSIONS.put(453, "19w03a - [1.14]");
        VERSIONS.put(452, "19w02a - [1.14]");
        VERSIONS.put(451, "18w50a - [1.14]");
        VERSIONS.put(450, "18w49a - [1.14]");
        VERSIONS.put(449, "18w48b - [1.14]");
        VERSIONS.put(448, "18w48a - [1.14]");
        VERSIONS.put(447, "18w47b - [1.14]");
        VERSIONS.put(446, "18w47a - [1.14]");
        VERSIONS.put(445, "18w46a - [1.14]");
        VERSIONS.put(444, "18w45a - [1.14]");
        VERSIONS.put(443, "18w44a - [1.14]");
        VERSIONS.put(442, "18w43c - [1.14]");
        VERSIONS.put(441, "18w43b - [1.14]");
        VERSIONS.put(404, "1.13.2");
        VERSIONS.put(403, "1.13.2-pre2");
        VERSIONS.put(402, "1.13.2-pre1");
        VERSIONS.put(401, "1.13.1");
        VERSIONS.put(400, "1.13.1-pre2");
        VERSIONS.put(399, "1.13.1-pre1");
        VERSIONS.put(398, "18w33a - [1.13]");
        VERSIONS.put(397, "18w32a - [1.13]");
        VERSIONS.put(396, "18w31a - [1.13]");
        VERSIONS.put(395, "18w30b - [1.13]");
        VERSIONS.put(394, "18w30a - [1.13]");
        VERSIONS.put(393, "1.13");
        VERSIONS.put(392, "1.13-pre10");
        VERSIONS.put(391, "1.13-pre9");
        VERSIONS.put(390, "1.13-pre8");
        VERSIONS.put(389, "1.13-pre7");
        VERSIONS.put(388, "1.13-pre6");
        VERSIONS.put(387, "1.13-pre5");
        VERSIONS.put(386, "1.13-pre4");
        VERSIONS.put(385, "1.13-pre3");
        VERSIONS.put(384, "1.13-pre2");
        VERSIONS.put(383, "1.13-pre1");
        VERSIONS.put(382, "18w22c - [1.13]");
        VERSIONS.put(381, "18w22b - [1.13]");
        VERSIONS.put(380, "18w22a - [1.13]");
        VERSIONS.put(379, "18w21b - [1.13]");
        VERSIONS.put(378, "18w21a - [1.13]");
        VERSIONS.put(377, "18w20c - [1.13]");
        VERSIONS.put(376, "18w20b - [1.13]");
        VERSIONS.put(375, "18w20a - [1.13]");
        VERSIONS.put(374, "18w19b - [1.13]");
        VERSIONS.put(373, "18w19a - [1.13]");
        VERSIONS.put(372, "18w16a - [1.13]");
        VERSIONS.put(371, "18w15a - [1.13]");
        VERSIONS.put(370, "18w14b - [1.13]");
        VERSIONS.put(369, "18w14a - [1.13]");
        VERSIONS.put(368, "18w11a - [1.13]");
        VERSIONS.put(367, "18w10d - [1.13]");
        VERSIONS.put(366, "18w10c - [1.13]");
        VERSIONS.put(365, "18w10b - [1.13]");
        VERSIONS.put(364, "18w10a - [1.13]");
        VERSIONS.put(363, "18w09a - [1.13]");
        VERSIONS.put(362, "18w08b - [1.13]");
        VERSIONS.put(361, "18w08a - [1.13]");
        VERSIONS.put(360, "18w07c - [1.13]");
        VERSIONS.put(359, "18w07b - [1.13]");
        VERSIONS.put(358, "18w07a - [1.13]");
        VERSIONS.put(357, "18w06a - [1.13]");
        VERSIONS.put(356, "18w05a - [1.13]");
        VERSIONS.put(355, "18w03b - [1.13]");
        VERSIONS.put(354, "18w03a - [1.13]");
        VERSIONS.put(353, "18w02a - [1.13]");
        VERSIONS.put(352, "18w01a - [1.13]");
        VERSIONS.put(351, "17w50a - [1.13]");
        VERSIONS.put(350, "17w49b - [1.13]");
        VERSIONS.put(349, "17w49a - [1.13]");
        VERSIONS.put(348, "17w48a - [1.13]");
        VERSIONS.put(347, "17w47b - [1.13]");
        VERSIONS.put(346, "17w47a - [1.13]");
        VERSIONS.put(345, "17w46a - [1.13]");
        VERSIONS.put(344, "17w45b - [1.13]");
        VERSIONS.put(343, "17w45a - [1.13]");
        VERSIONS.put(342, "17w43b - [1.13]");
        VERSIONS.put(341, "17w43a - [1.13]");
        VERSIONS.put(340, "1.12.2");
        VERSIONS.put(339, "1.12.2-pre2");
        VERSIONS.put(338, "1.12.1");
        VERSIONS.put(337, "1.12.1-pre1");
        VERSIONS.put(336, "17w31a - [1.12]");
        VERSIONS.put(335, "1.12");
        VERSIONS.put(334, "1.12-pre7");
        VERSIONS.put(333, "1.12-pre6");
        VERSIONS.put(332, "1.12-pre5");
        VERSIONS.put(331, "1.12-pre4");
        VERSIONS.put(330, "1.12-pre3");
        VERSIONS.put(329, "1.12-pre2");
        VERSIONS.put(328, "1.12-pre1");
        VERSIONS.put(327, "17w18b - [1.12]");
        VERSIONS.put(326, "17w18a - [1.12]");
        VERSIONS.put(325, "17w17b - [1.12]");
        VERSIONS.put(324, "17w17a - [1.12]");
        VERSIONS.put(323, "17w16b - [1.12]");
        VERSIONS.put(322, "17w16a - [1.12]");
        VERSIONS.put(321, "17w15a - [1.12]");
        VERSIONS.put(320, "17w14a - [1.12]");
        VERSIONS.put(319, "17w13b - [1.12]");
        VERSIONS.put(318, "17w13a - [1.12]");
        VERSIONS.put(317, "17w06a - [1.12]");
        VERSIONS.put(316, "1.11.2");
        VERSIONS.put(315, "1.11");
        VERSIONS.put(314, "1.11-pre1");
        VERSIONS.put(313, "16w44a - [1.11]");
        VERSIONS.put(312, "16w42a - [1.11]");
        VERSIONS.put(311, "16w41a - [1.11]");
        VERSIONS.put(310, "16w40a - [1.11]");
        VERSIONS.put(309, "16w39c - [1.11]");
        VERSIONS.put(308, "16w39b - [1.11]");
        VERSIONS.put(307, "16w39a - [1.11]");
        VERSIONS.put(306, "16w38a - [1.11]");
        VERSIONS.put(305, "16w36a - [1.11]");
        VERSIONS.put(304, "16w35a - [1.11]");
        VERSIONS.put(303, "16w33a - [1.11]");
        VERSIONS.put(302, "16w32b - [1.11]");
        VERSIONS.put(301, "16w32a - [1.11]");
        VERSIONS.put(210, "1.10");
        VERSIONS.put(205, "1.10-pre2");
        VERSIONS.put(204, "1.10-pre1");
        VERSIONS.put(203, "16w21b - [1.10]");
        VERSIONS.put(202, "16w21a - [1.10]");
        VERSIONS.put(201, "16w20a - [1.10]");
        VERSIONS.put(110, "1.9.3");
        VERSIONS.put(109, "1.9.2");
        VERSIONS.put(108, "1.9.1-pre2");
        VERSIONS.put(107, "1.9");
        VERSIONS.put(106, "1.9-pre4");
        VERSIONS.put(105, "1.9-pre3");
        VERSIONS.put(104, "1.9-pre2");
        VERSIONS.put(103, "1.9-pre1");
        VERSIONS.put(102, "16w07b - [1.9]");
        VERSIONS.put(101, "16w07a - [1.9]");
        VERSIONS.put(100, "16w06a - [1.9]");
        VERSIONS.put(99, "16w05b - [1.9]");
        VERSIONS.put(98, "16w05a - [1.9]");
        VERSIONS.put(97, "16w04a - [1.9]");
        VERSIONS.put(96, "16w03a - [1.9]");
        VERSIONS.put(95, "16w02a - [1.9]");
        VERSIONS.put(94, "15w51b - [1.9]");
        VERSIONS.put(93, "15w51a - [1.9]");
        VERSIONS.put(92, "15w50a - [1.9]");
        VERSIONS.put(91, "15w49b - [1.9]");
        VERSIONS.put(90, "15w49a - [1.9]");
        VERSIONS.put(89, "15w47c - [1.9]");
        VERSIONS.put(88, "15w47b - [1.9]");
        VERSIONS.put(87, "15w47a - [1.9]");
        VERSIONS.put(86, "15w46a - [1.9]");
        VERSIONS.put(85, "15w45a - [1.9]");
        VERSIONS.put(84, "15w44b - [1.9]");
        VERSIONS.put(83, "15w44a - [1.9]");
        VERSIONS.put(82, "15w43c - [1.9]");
        VERSIONS.put(81, "15w43b - [1.9]");
        VERSIONS.put(80, "15w43a - [1.9]");
        VERSIONS.put(79, "15w42a - [1.9]");
        VERSIONS.put(78, "15w41b - [1.9]");
        VERSIONS.put(77, "15w41a - [1.9]");
        VERSIONS.put(76, "15w40b - [1.9]");
        VERSIONS.put(75, "15w40a - [1.9]");
        VERSIONS.put(74, "15w39a - [1.9]");
        VERSIONS.put(73, "15w38b - [1.9]");
        VERSIONS.put(72, "15w38a - [1.9]");
        VERSIONS.put(71, "15w37a - [1.9]");
        VERSIONS.put(70, "15w36d - [1.9]");
        VERSIONS.put(69, "15w36c - [1.9]");
        VERSIONS.put(68, "15w36b - [1.9]");
        VERSIONS.put(67, "15w36a - [1.9]");
        VERSIONS.put(66, "15w35e - [1.9]");
        VERSIONS.put(65, "15w35d - [1.9]");
        VERSIONS.put(64, "15w35c - [1.9]");
        VERSIONS.put(63, "15w35b - [1.9]");
        VERSIONS.put(62, "15w35a - [1.9]");
        VERSIONS.put(61, "15w34d - [1.9]");
        VERSIONS.put(60, "15w34c - [1.9]");
        VERSIONS.put(59, "15w34b - [1.9]");
        VERSIONS.put(58, "15w34a - [1.9]");
        VERSIONS.put(57, "15w33c - [1.9]");
        VERSIONS.put(56, "15w33b - [1.9]");
        VERSIONS.put(55, "15w33a - [1.9]");
        VERSIONS.put(54, "15w32c - [1.9]");
        VERSIONS.put(53, "15w32b - [1.9]");
        VERSIONS.put(52, "15w32a - [1.9]");
        VERSIONS.put(51, "15w31c - [1.9]");
        VERSIONS.put(50, "15w31b - [1.9]");
        VERSIONS.put(49, "15w31a - [1.9]");
        VERSIONS.put(48, "15w14a - [1.9]");
    }
}